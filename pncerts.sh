#!/usr/bin/env bash

cp .local_pki/root_ca.crt ../ida-hub-acceptance-tests/truststores

pushd ../ida-hub-acceptance-tests/truststores/

keytool -keystore ida_rp_truststore.ts -delete -alias pnrootca -storepass puppet

keytool -import -trustcacerts -alias pnrootca -file root_ca.crt -keystore ida_rp_truststore.ts -storepass puppet

keytool -keystore ida_hub_truststore.ts -delete -alias pnrootca -storepass puppet

keytool -import -trustcacerts -alias pnrootca -file root_ca.crt -keystore ida_hub_truststore.ts -storepass puppet

keytool -keystore ida_truststore.ts -delete -alias pnrootca -storepass puppet

keytool -import -trustcacerts -alias pnrootca -file root_ca.crt -keystore ida_truststore.ts -storepass puppet

popd

echo "----------------"
cat .local_pki/proxy-node-configmap.yaml | grep HUB_FACING_SIGNING_CERT | sed 's/.*: //' | base64 -D
echo "----------------"
cat .local_pki/proxy-node-configmap.yaml | grep HUB_FACING_ENCRYPTION_CERT | sed 's/.*: //' | base64 -D
echo "----------------"

#cp .local_pki/stub_idp_signing.crt ../ida-hub-acceptance-tests/certs
#cp .local_pki/stub_idp_signing.pk8 ../ida-hub-acceptance-tests/keys


