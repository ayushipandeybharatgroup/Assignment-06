def call(String configPath = 'env/prod/config.groovy') {
    // Step 1: Load config
    def config = libraryResource(configPath)
    config = evaluate(new GroovyShell().parse(config))

    // Step 2: Clone repo
    stage('Clone Repo') {
        echo 'Cloning codebase...'
        checkout scm
    }

    // Step 3: Approval (if KEEP_APPROVAL_STAGE is true)
    if (config.KEEP_APPROVAL_STAGE.toBoolean()) {
        stage('Approval') {
            timeout(time: 5, unit: 'MINUTES') {
                input message: "Proceed with playbook execution for ${config.ENVIRONMENT}?"
            }
        }
    }

    // Step 4: Run Ansible Playbook
    stage('Execute Playbook') {
        echo "Running Ansible Playbook in ${config.ENVIRONMENT}"
        sh "ansible-playbook -i inventory site.yml"
    }

    // Step 5: Slack Notification
    stage('Notify') {
        echo "Sending notification to ${config.SLACK_CHANNEL_NAME}"
        slackSend(
            channel: config.SLACK_CHANNEL_NAME,
            message: config.ACTION_MESSAGE,
            color: "good"
        )
    }
}
