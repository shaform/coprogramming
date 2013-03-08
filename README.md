NAME
----

coprogramming - collaborative programming made easy

DESCRIPTION
-----------

coprogramming is a tool that makes it insanely easy for beginning
programmers to collaborate on small projects.


Client Secrets File
-------------------
You must have a client secrets file in order to  run the program.
The file must have the following syntax:

    {
        "web": {
            "client_id": "_CLIENT_ID_",
            "client_secret": "_CLIENT_SECRET_",
            "auth_uri": "https://accounts.google.com/o/oauth2/auth",
            "token_uri": "https://accounts.google.com/o/oauth2/token"
        }
    }

The file must be saved as:

    src/net/coprg/coprg/resource/client_secrets.json

For how to create a client id, see: [Enable the drive API] (https://developers.google.com/drive/quickstart-java#step_1_enable_the_drive_api).
