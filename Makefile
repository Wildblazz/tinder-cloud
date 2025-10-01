KUBECTL = kubectl apply -f
KUSTOMIZE = kubectl apply -k

# Run all setup steps
all: deploy-resources

deploy-resources: deploy-apps
deploy-apps: apply-projects
apply-projects: argocd-init
argocd-init: minikube-init

minikube-init:
	bash deploy/eks/minikube-init.sh

argocd-init:
	bash deploy/eks/argocd-init.sh

generate-secrets:
	bash deploy/eks/apply-secrets.sh

# ArgoCD
apply-projects:
	$(KUBECTL) deploy/eks/argocd/app-projects/infrastructure.yaml
	$(KUBECTL) deploy/eks/argocd/app-projects/tinder-cloud.yaml

deploy-apps:
	$(KUBECTL) deploy/eks/argocd/apps/infrastructure-app.yaml
	$(KUBECTL) deploy/eks/argocd/apps/tinder-app.yaml

deploy-resources:
	$(KUSTOMIZE) deploy/eks/argocd/base/infrastructure/
	$(KUSTOMIZE) deploy/eks/argocd/base/tinder-app/

.PHONY: all minikube-init argocd-init generate-secrets apply-projects deploy-apps deploy-resources
