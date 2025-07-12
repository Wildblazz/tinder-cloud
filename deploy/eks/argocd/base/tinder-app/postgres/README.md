# DB deployment

After changing the init.sql file, the changes will not be reflected in the configmap unless it is regenerated.
Steps:
* Generate configmap: `kubectl create configmap postgres-init-script --from-file=init.sql --dry-run=client -o yaml > configmap.yaml`
* Add a namespace to the generated configmap.yaml `metadata` section: `namespace: tinder-space`
