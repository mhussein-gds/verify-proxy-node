#!/usr/bin/env bash

TRANSLATOR_CONFIGURATIONS="dropwizard opensaml verify_saml common soft_hsm"

exit_code=0

function print_banner() {
    local project=$1;
    echo "######################################################################"
    echo "### Testing dependencies for $project"
    echo "######################################################################"
}

function test_configuration() {
    local config=$1;
    local sub_project=$2;
    print_banner "$config gradle configuration"
    snyk test --gradle-sub-project=proxy-node-translator -- --configuration="$config"
    if [[ $? -gt 0 ]]; then
        exit_code=1
    fi
}

for configuration in $TRANSLATOR_CONFIGURATIONS; do
   test_configuration $configuration proxy-node-translator
done

test_configuration eidas_saml eidas-saml-parser

print_banner "pki"
pushd pki; snyk test; popd

exit $exit_code