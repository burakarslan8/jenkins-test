import jenkins
import time
import boto3
from botocore.exceptions import ClientError

JENKINS_URL = "http://localhost:8080"
USERNAME = "burak"
JENKINS_API_TOKEN = "113fda31f2732987e3360d76e587ed429f" # shouldn't be stored in code
JOB_NAME = "mailtest"
IS_FAKE = "false"

SENDER = "TOKEN INC <example@tokeninc.com>"
RECIPIENT = "example@tokeninc.com"
AWS_REGION = "eu-central-1"

def build_pipeline():
    server = jenkins.Jenkins(JENKINS_URL, username=USERNAME, password=JENKINS_API_TOKEN)
    server.build_job(JOB_NAME,{'IS_FAKE': IS_FAKE})
    print("Build started.")

    # wait for the task to finish
    queue_info=server.get_queue_info()
    while queue_info:
        queue_info=server.get_queue_info()

    time.sleep(1) # wait for the build to be marked as completed

    build_number = server.get_job_info(JOB_NAME)['lastCompletedBuild']['number']
    result = server.get_build_info(JOB_NAME, build_number)['result']
    build_time = server.get_build_info(JOB_NAME, build_number)['duration']/1000.0

    return build_number, result, build_time

def send_email(build_number, result, build_time):
    client = boto3.client('ses',region_name=AWS_REGION)

    subject = f"Pipeline: {JOB_NAME} - Build #{build_number} - {result} | {build_time}s"
    body_text = "The build is successful." if result == "SUCCESS" else "The build has failed."
    charset = "UTF-8"


    try:
        response = client.send_email(
            Destination = {
                'ToAddresses': [
                    RECIPIENT,
                ],
            },
            Message = {
                'Body': {
                    'Text': {
                        'Charset': charset,
                        'Data': body_text,
                    },
                },
                'Subject': {
                    'Charset': charset,
                    'Data': subject,
                },
            },
            Source = SENDER,
        )
    except ClientError as e:
        print(e.response['Error']['Message'])
    else:
        print("Email sent! Message ID:")
        print(response['MessageId'])
    

if __name__ == "__main__":
    build_number, result, build_time = build_pipeline()
    print(f"Pipeline: {JOB_NAME} - Build #{build_number} - {result} | {build_time}s")

    # email notification
    if IS_FAKE == "false":
        send_email(build_number, result, build_time)
    else:
        print("Email not sent because the build is fake.")
