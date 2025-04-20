def k8s_manifests(imageTag) {
    withCredentials([usernamePassword(
        credentialsId: 'github-credentials', // The ID of the credentials stored in Jenkins
        usernameVariable: 'GIT_USERNAME',    // The environment variable to store the GitHub username
        passwordVariable: 'GIT_PASSWORD'     // The environment variable to store the GitHub password or token
    )]) {
        // The shell block that uses the Git credentials
        sh """
            # Git Configuration
            git config user.name "Jenkins CI"
            git config user.email "dakshsawhney2@gmail.com"
            
            # Replace image tags in your app manifests (hardcoded for WearSphere)
            sed -i "s|image: dakshsawhneyy/wearsphere-backend:.*|image: dakshsawhneyy/wearsphere-backend:${imageTag}|g" kubernetes/backend-deployment.yaml
            sed -i "s|image: dakshsawhneyy/wearsphere-frontend:.*|image: dakshsawhneyy/wearsphere-frontend:${imageTag}|g" kubernetes/frontend-deployment.yaml
            sed -i "s|image: dakshsawhneyy/wearsphere-admin:.*|image: dakshsawhneyy/wearsphere-admin:${imageTag}|g" kubernetes/admin-deployment.yaml
            
            # Check if there are changes to commit
            if git diff --quiet; then
                echo "✅ No changes to commit."
            else
                git add kubernetes/*.yaml
                git commit -m "🔁 Update image tags to ${imageTag} [ci skip]"
                git remote set-url origin https://\${GIT_USERNAME}:\${GIT_PASSWORD}@github.com/dakshsawhneyy/WearSphere-Ecommerce-MERN.git
                git push origin HEAD:master
            fi
        """
    }
}
