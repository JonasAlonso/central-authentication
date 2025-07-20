# Generate a code_verifier (43-128 characters)
export VERIFIER=$(openssl rand -base64 96 | tr -dc 'a-zA-Z0-9' | head -c 64)

# Create the SHA256 hash and base64url encode it
export CHALLENGE=$(echo -n $VERIFIER | openssl dgst -sha256 -binary | openssl base64 | tr '+/' '-_' | tr -d '=')
