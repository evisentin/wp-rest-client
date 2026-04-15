# Define a build argument to control which WordPress image version to use.
# Defaults to "latest" if not overridden from code.
ARG WORDPRESS_VERSION=latest

# Use the selected WordPress base image.
FROM wordpress:${WORDPRESS_VERSION}

# Install OpenSSL so we can generate a self-signed certificate.
RUN apt-get update && \
    apt-get install -y --no-install-recommends openssl

# Enable Apache's SSL module so the server can handle HTTPS (port 443).
# Create a dedicated directory to store the TLS private key and certificate.
# Generate a self-signed X.509 certificate for local development/testing:
# - no passphrase (-nodes) so Apache can start without manual input
# - valid for 365 days
# - 2048-bit RSA key
# - subject is provided inline to avoid interactive prompts
RUN a2enmod ssl && \
    mkdir -p /etc/apache2/ssl && \
    openssl req -x509 -nodes -days 365 \
      -newkey rsa:2048 \
      -keyout /etc/apache2/ssl/server.key \
      -out /etc/apache2/ssl/server.crt \
      -subj "/C=CH/ST=ZH/L=Zurich/O=Local Test/OU=QA/CN=localhost"

# Copy the custom Apache HTTPS virtual host configuration into the image.
COPY wordpress-ssl.conf /etc/apache2/sites-available/wordpress-ssl.conf

# Enable the SSL site in Apache.
RUN a2ensite wordpress-ssl.conf

# Expose standard HTTP and HTTPS ports.
EXPOSE 80 443
