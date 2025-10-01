# Generate secrets and add into cluster:
* In `/deploy/eks/argocd/base/tinder-app/secrets` create a file `.env.secrets` Example file: `.env.secrets.example`
* `chmod +x deploy/eks/apply-secrets.sh`
* `./deploy/eks/apply-secrets.sh`
