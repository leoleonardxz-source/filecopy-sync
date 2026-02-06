package com.starocean.fileCopy.remote;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
public class RemoteApiClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteApiClient.class);

	private final WebClient apiWebClient;
	private final Duration defaultTimeout = Duration.ofSeconds(30);

	public RemoteApiClient(@Qualifier("apiWebClient") WebClient apiWebClient) {
		this.apiWebClient = apiWebClient;
	}

	public <T> Mono<T> call(RemoteApiDef api,
			Map<String, ?> query,
			Map<String, String> headers,
			Object body,
			Class<T> respType) {
		return call(api.method(), api.path(), query, headers, body, respType, defaultTimeout, null, api.desc());
	}

	public <T> Mono<T> call(HttpMethod method,
			String path,
			Map<String, ?> query,
			Map<String, String> headers,
			Object body,
			Class<T> respType,
			Duration timeout,
			Retry retryPolicy) {
		return call(method, path, query, headers, body, respType, timeout, retryPolicy, null);
	}

	private <T> Mono<T> call(HttpMethod method,
			String path,
			Map<String, ?> query,
			Map<String, String> headers,
			Object body,
			Class<T> respType,
			Duration timeout,
			Retry retryPolicy,
			String desc) {

		Map<String, ?> q = (query == null) ? Collections.emptyMap() : query;
		Map<String, String> h = (headers == null) ? Collections.emptyMap() : headers;
		Duration t = (timeout == null) ? defaultTimeout : timeout;

		WebClient.RequestBodySpec spec = apiWebClient
				.method(method)
				.uri(uriBuilder -> {
					uriBuilder.path(path);
					if (!CollectionUtils.isEmpty(q)) {
						q.forEach((k, v) -> {
							if (v != null) uriBuilder.queryParam(k, v);
						});
					}
					return uriBuilder.build();
				})
				.accept(MediaType.APPLICATION_JSON);

		if (!h.isEmpty()) {
			spec.headers(httpHeaders -> h.forEach(httpHeaders::add));
		}

		if (body != null) {
			spec.contentType(MediaType.APPLICATION_JSON);
			spec.body(BodyInserters.fromValue(body));
		}

		String tag = (desc == null || desc.isBlank()) ? "" : (" desc=" + desc);

		Mono<T> mono = spec.exchangeToMono(resp -> handleResponse(method, path, resp, respType))
				.timeout(t)
				.doOnSubscribe(s -> LOGGER.debug("Calling api: method={} path={}{}", method, path, tag));

		if (retryPolicy != null) {
			mono = mono.retryWhen(retryPolicy);
		}
		LOGGER.info("Prepared RemoteApi call: method={} path={}{} timeout={}s , resoult = {}", method, path, tag, t.getSeconds(),mono);
		return mono;
	}

	private <T> Mono<T> handleResponse(HttpMethod method,
			String path,
			ClientResponse resp,
			Class<T> respType) {
		int code = resp.statusCode().value();

		if (code >= 200 && code < 300) {
			if (respType == Void.class) {
				return resp.bodyToMono(Void.class).then(Mono.empty());
			}
			return resp.bodyToMono(respType);
		}

		return resp.bodyToMono(String.class)
				.defaultIfEmpty("")
				.flatMap(body -> {
					String snippet = body.length() > 800 ? body.substring(0, 800) + "..." : body;
					String msg = "RemoteApi call failed: method=" + method +
							", path=" + path +
							", status=" + code +
							", body=" + snippet;
					return Mono.error(new RemoteApiException(code, msg));
				});
	}

	public static Retry defaultRetry() {
		return Retry.backoff(3, Duration.ofSeconds(1)).maxBackoff(Duration.ofSeconds(5));
	}

	public static class RemoteApiException extends RuntimeException {
		private static final long serialVersionUID = 5782552633065129952L;
		private final int statusCode;

		public RemoteApiException(int statusCode, String message) {
			super(message);
			this.statusCode = statusCode;
		}

		public int getStatusCode() {
			return statusCode;
		}
	}
}
