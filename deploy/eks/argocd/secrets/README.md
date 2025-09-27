# Generate secrets and add into cluster:
  * In `/eks/argocd/secrets/` create a file `.env.secrets` Example file: `.env.secrets.example`
  * `chmod +x deploy/eks/argocd/secrets/apply-secret.sh`
  * `./deploy/eks/argocd/secrets/apply-secret.sh`
