import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email import encoders
import os
import shutil
import json
from jinja2 import Template

# Embedded HTML template content
html_template_content = """
<!DOCTYPE html>
<html>
<head>
  <style>
    table {
      border-collapse: collapse;
      width: 100%;
    }
    th, td {
      border: 1px solid #ddd;
      padding: 8px;
      text-align: left;
    }
    th {
      background-color: #f2f2f2;
    }
  </style>
</head>
<body>

<h2>Test Results</h2>

<table>
  <thead>
    <tr>
      <th>S No</th>
      <th>Scenario Name</th>
      <th>TestStepsPassed</th>
      <th>TestStepsFailed</th>
      <th>Status</th>
      <th>Error Message</th>
    </tr>
  </thead>
  <tbody>
    {% for scenario in scenarios %}
      <tr>
        <td>{{ loop.index }}</td>
        <td>{{ scenario.name }}</td>
        <td>{{ scenario.steps_passed }}</td>
        <td>{{ scenario.steps_failed }}</td>
        <td>{{ scenario.status }}</td>
        <td>{{ scenario.error_message }}</td>
      </tr>
    {% endfor %}
  </tbody>
</table>

</body>
</html>
"""

def smtp_password():
    password = os.getenv("SMTP_EMAIL_PASSWORD")
    if password is None:
        raise RuntimeError("SMTP password is not available. Please set the environment variable SMTP_EMAIL_PASSWORD.")
    return password
    
def generate_html_report(json_data):
    # Extract relevant information from the JSON data
    scenarios = []
    for scenario in json_data[0].get("elements", []):
        scenario_info = {
            "name": scenario["name"],
            "steps_passed": sum(1 for step in scenario["steps"] if step["result"]["status"] == "passed"),
            "steps_failed": sum(1 for step in scenario["steps"] if step["result"]["status"] == "failed"),
            "status": "Failed" if any(step["result"]["status"] == "failed" for step in scenario["steps"]) else "Passed",
            "error_message": next((step["result"]["error_message"] for step in scenario["steps"] if step["result"]["status"] == "failed"), ""),
        }
        scenarios.append(scenario_info)

    # Create a Template object from the embedded HTML template content
    template = Template(html_template_content)

    # Render the HTML template with the extracted data
    rendered_html = template.render(scenarios=scenarios)

    # Save the rendered HTML to a file in the project directory
    with open("test_results.html", "w") as output_file:
        output_file.write(rendered_html)

    return rendered_html

def send_test_results_email(html_content, attachment_path):
    # Sender's email address
    username = "singh.raja27@outlook.com"
    # Sender's password
    password = smtp_password()

    # Recipient's email address
    to_email = "singh.saurav@icloud.com"

    # SMTP server settings
    host = "smtp.office365.com"
    port = 587

    # Create a MIMEText object for the email content
    message = MIMEMultipart()
    message['From'] = username
    message['To'] = to_email
    message['Subject'] = "Test Results Email"

    # Attach the HTML content to the email
    message.attach(MIMEText(html_content, 'html'))

    # Create a ZIP file containing the reports
    shutil.make_archive(attachment_path, "zip", "reports")

    # Attach the zip file
    attachment = MIMEBase('application', 'zip')
    with open(f"{attachment_path}.zip", "rb") as attachment_file:
        attachment.set_payload(attachment_file.read())
    encoders.encode_base64(attachment)
    attachment.add_header('Content-Disposition', f'attachment; filename={os.path.basename(f"{attachment_path}.zip")}')
    message.attach(attachment)

    # Set up the SMTP connection
    with smtplib.SMTP(host, port) as server:
        # Start TLS for security
        server.starttls()
        # Login to the SMTP server
        server.login(username, password)
        # Send the email
        server.sendmail(username, to_email, message.as_string())

    print("Test results email sent successfully.")

if __name__ == "__main__":
    # Read the JSON file
    with open("target/cucumber.json", "r") as json_file:
        json_data = json.load(json_file)

    # Generate and save the HTML report
    html_content = generate_html_report(json_data)

    # Example usage
    attachment_path = "reports"  # Update this path based on your GitLab CI/CD configuration
    send_test_results_email(html_content, attachment_path)
