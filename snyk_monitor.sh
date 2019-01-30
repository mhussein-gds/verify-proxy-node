#!/usr/bin/env bash

TRANSLATOR_CONFIGURATIONS="dropwizard opensaml verify_saml common soft_hsm"

function print_banner() {
    local project=$1;
    echo "######################################################################"
    echo "### Monitoring dependencies for $project"
    echo "######################################################################"
}

function monitor_configuration() {
    local config=$1;
    local sub_project=$2;
    print_banner "$config gradle configuration"
    snyk monitor --gradle-sub-project=$sub_project --project-name="$config"-config -- --configuration="$config"
}

for configuration in $TRANSLATOR_CONFIGURATIONS; do
    monitor_configuration $configuration proxy-node-translator
done;

monitor_configuration eidas_saml eidas-saml-parser

print_banner "pki"
pushd pki; snyk monitor --project-name=pki; popd