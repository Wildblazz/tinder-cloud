# DB deployment

After changing the init.sql file, the changes will not be reflected in the configmap unless it is regenerated.
Steps:
* Generate configmap: `kubectl create configmap postgres-init-script --from-file=init.sql -n tinder-space --dry-run=client -o yaml > configmap.yaml`
