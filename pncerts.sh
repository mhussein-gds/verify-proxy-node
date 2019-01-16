#!/usr/bin/env bash

cp .local_pki/root_ca.crt /Users/danielbesbrode/Code/Verify/ida-hub-acceptance-tests/truststores/root_ca.crt

cd /Users/danielbesbrode/Code/Verify/ida-hub-acceptance-tests/truststores/

keytool -keystore ida_rp_truststore.ts -delete -alias pnrootca -storepass puppet

keytool -import -trustcacerts -alias pnrootca -file root_ca.crt -keystore ida_rp_truststore.ts -storepass puppet

cd -

echo "----------------"
cat .local_pki/proxy-node-configmap.yaml | grep HUB_FACING_SIGNING_CERT
echo "----------------"
cat .local_pki/proxy-node-configmap.yaml | grep HUB_FACING_ENCRYPTION_CERT
echo "----------------"

