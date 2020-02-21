# JavaFTP
Java FTP client for sending files/make new folders if not exists

# Algorithm
1. List content of the local directory.
2. For each item in the local directory:
    If the item is a file, upload the file to the server.
    If the item is a directory:
        Create the directory on the server if the directory is not exists
        Upload this sub directory by repeating the step 1, 2 and 3.
Return if the directory is empty or if the last item is processed.