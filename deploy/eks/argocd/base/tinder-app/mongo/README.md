# DB deployment

After changing the init.js file, the changes will not be reflected in the configmap unless it is regenerated.
Steps:
* Generate configmap: `kubectl create configmap mongo-init-script --from-file=init.js -n tinder-space --dry-run=client -o yaml > configmap.yaml`
