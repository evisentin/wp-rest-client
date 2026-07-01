/**
 * Authorization header provider abstractions and implementations.
 *
 * <p>This package contains components responsible for transforming authentication strategies into HTTP
 * {@code Authorization} header values.</p>
 *
 * <p>The central contract is
 * {@link io.github.evisentin.wordpress.rest.client.domain.auth.header.AuthenticationHeaderProvider}, with
 * implementations for both Basic and JWT bearer authentication.</p>
 */
package io.github.evisentin.wordpress.rest.client.domain.auth.header;
