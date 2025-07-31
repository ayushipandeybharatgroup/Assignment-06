def getConfig() {
    return [
        SLACK_CHANNEL_NAME  : "build-status",
        ENVIRONMENT         : "prod",
        CODE_BASE_PATH      : "env/prod",
        ACTION_MESSAGE      : "Redis deployment initiated for production!",
        KEEP_APPROVAL_STAGE : true
    ]
}
