/**
 * Domain model for the WordPress REST client.
 *
 * <p>This module contains the core abstractions and value objects used by the client independently from any specific
 * HTTP implementation or transport layer.</p>
 *
 * <p>It includes:</p>
 * <ul>
 *   <li>authentication strategies and authorization support</li>
 *   <li>request and response domain models</li>
 *   <li>client-independent service contracts</li>
 *   <li>shared domain utilities and exceptions</li>
 * </ul>
 *
 * <p>The domain module is designed to be transport-agnostic and reusable across different client adapter implementations.</p>
 */
package io.github.evisentin.wordpress.client.domain;
