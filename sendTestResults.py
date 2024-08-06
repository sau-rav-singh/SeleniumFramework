import json
import requests
import os
import shutil
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

def generate_html_report(json_data):
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
    template = Template(html_template_content)
    rendered_html = template.render(scenarios=scenarios)
    with open("test_results.html", "w") as output_file:
        output_file.write(rendered_html)
    return rendered_html

def send_test_results_email(html_content, attachment_path):
    mailgun_api_key = os.environ.get("MAILGUN_API_KEY")
    mailgun_domain = os.environ.get("MAILGUN_DOMAIN")
    recipient_email = os.environ.get("TO_EMAIL")
    mailgun_url = f"https://api.mailgun.net/v3/{mailgun_domain}/messages"
    message_data = {
        "from": "Gitlab Runner <USER@YOURDOMAIN.COM>",
        "to": recipient_email,
        "subject": "Test Results",
        "html": html_content,
    }
    shutil.make_archive(attachment_path, "zip", "reports")
    with open(f"{attachment_path}.zip", "rb") as attachment:
        files = {"attachment": (os.path.basename(f"{attachment_path}.zip"), attachment)}
        # Send the email using Mailgun API with attachment
        response = requests.post(
            mailgun_url,
            auth=("api", mailgun_api_key),
            data=message_data,
            files=files,
        )
    print(response.json())

if __name__ == "__main__":
    with open("target/cucumber.json", "r") as json_file:
        json_data = json.load(json_file)
    html_content = generate_html_report(json_data)
    attachment_path = "reports"  # Update this path based on your GitLab CI/CD configuration
    send_test_results_email(html_content, attachment_path)