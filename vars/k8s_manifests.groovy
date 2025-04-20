def call(String imageTag) {
    withCredentials([usernamePassword(
        credentialsId: 'github-credentials',
        usernameVariable: 'GIT_USERNAME',
        passwordVariable: 'GIT_PASSWORD'
    )]) {
        sh """
            git config user.name "Jenkins CI"
            git config user.email "dakshsawhney2@gmail.com"
            sed -i "s|image: dakshsawhneyy/wearsphere-backend:.*|image: dakshsawhneyy/wearsphere-backend:${imageTag}|g" kubernetes/backend-deployment.yml
            sed -i "s|image: dakshsawhneyy/wearsphere-frontend:.*|image: dakshsawhneyy/wearsphere-frontend:${imageTag}|g" kubernetes/frontend_deployment.yml
            sed -i "s|image: dakshsawhneyy/wearsphere-admin:.*|image: dakshsawhneyy/wearsphere-admin:${imageTag}|g" kubernetes/admin_deployment.yml

            if git diff --quiet; then
                echo "✅ No changes to commit."
            else
                git add kubernetes/*.yaml
                git commit -m "🔁 Update image tags to ${imageTag} [ci skip]"
                git remote set-url origin https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/dakshsawhneyy/WearSphere-Ecommerce-MERN.git
                git push origin HEAD:master
            fi
        """
    }
}
