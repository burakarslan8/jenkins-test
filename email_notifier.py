import sys
import boto3
from botocore.exceptions import ClientError

AWS_REGION = "eu-central-1"

sender = sys.argv[1]
recipient = sys.argv[2]
job_name = sys.argv[3]
build_number = sys.argv[4]
result = sys.argv[5]
build_time = sys.argv[6]


def send_email(sender, recipient, job_name, build_number, result, build_time):
    client = boto3.client('ses',region_name=AWS_REGION)

    subject = f"Pipeline: {job_name} - Build #{build_number} - {result} | {build_time}s"
    body_text = "The build is successful." if result == "SUCCESS" else "The build has failed."
    charset = "UTF-8"

    try:
        response = client.send_email(
            Destination = {
                'ToAddresses': [
                    recipient,
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
            Source = sender,
        )
    except ClientError as e:
        print(e.response['Error']['Message'])
    else:
        print(f"Email sent to {recipient}! Message ID:")
        print(response['MessageId'])
    

if __name__ == "__main__":
    send_email(sender, recipient, job_name, build_number, result, build_time)