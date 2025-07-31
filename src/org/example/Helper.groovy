package org.mycompany

def deploy(context, config) {
    context.stage('Clone Repository') {
        context.checkout context.scm
    }

    if (config.KEEP_APPROVAL_STAGE.toBoolean()) {
        context.stage('User Approval') {
            context.input message: "Do you want to continue with Redis deployment in ${config.ENVIRONMENT}?"
        }
    }

    context.stage('Execute Ansible Playbook') {
        context.sh """
            cd redis-playbook
            ansible-playbook -i ${config.CODE_BASE_PATH}/inventory redis.yml
        """
    }

    context.stage('Slack Notification') {
        context.slackSend(
            channel: config.SLACK_CHANNEL_NAME,
            message: config.ACTION_MESSAGE
        )
    }
}
