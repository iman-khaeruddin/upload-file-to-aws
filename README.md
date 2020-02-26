# Environment Variable Setup

The ~/.bash_profile ($HOME/.bash_profile) or ~/.profile file is executed when you login using console or remotely using ssh. Type the following command to edit ~/.bash_profile file, enter:

```
$ vi ~/.bash_profile
```

### 1. Server Port

Append the $SERVER_PORT settings, enter:

```
export SERVER_PORT = your_server_port
```

### 2. S3 Access Key

Append the $S3_ACCESS_KEY settings, enter:

```
export S3_ACCESS_KEY = your_s3_access_key
```

### 3. S3 Secret Key

Append the $S3_SECRET_KEY settings, enter:

```
export S3_SECRET_KEY = your_s3_secret_key
```

### 4. S3 Bucket Name

Append the $S3_BUCKET settings, enter:

```
export S3_BUCKET = your_s3_bucket_name
```

### 5. S3 Region

Append the $S3_REGION settings, enter:

```
export S3_REGION = your_s3_region
```