package com.twoweeksmc.web;

import java.util.function.Consumer;

public record Request(String path, String requestType, Consumer<HttpContext> context) {
}
