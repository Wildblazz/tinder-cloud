# KeyCloak deployment

After changing the tinder-realm.json file, the changes will not be reflected in the configmap unless it is regenerated.
Steps:
* Generate configmap: `kubectl create configmap keycloak-realm-config \
  --from-file=tinder-realm.json=deploy/eks/argocd/base/tinder-app/keycloak/tinder-realm.json \
  -n infrastructure \
  --dry-run=client -o yaml > deploy/eks/argocd/base/tinder-app/keycloak/configmap-realm.yaml`
* Add a namespace to the generated configmap.yaml `metadata` section: `namespace: infrastructure`
* Access to KeyCloak UI `minikube service keycloak -n infrastructure`
