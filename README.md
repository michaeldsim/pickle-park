# Pickle Park Discord Bot

A simple Discord bot to view park availability at **EE Robinson Park**.

## Usage

```
!pp <date>
```

- Dates are parsed by the **Natty** library.  
- Supports humanized dates like `tomorrow`, `next week`, etc.  
- Also accepts explicit dates like `08-20-2025` or assumes the current date if no date is passed in.  

<img width="415" height="499" alt="screenshot" src="https://github.com/user-attachments/assets/8e484716-3143-4b1d-88c8-b9cae837b2a7" />

## Run with Docker Compose

Image available on Docker Hub: [michaeldsim/pickle-park](https://hub.docker.com/r/michaeldsim/pickle-park)

```yaml
version: "3.9"

services:
  pickle-park:
    image: michaeldsim/pickle-park:latest
    container_name: pickle-park
    restart: unless-stopped
    environment:
      - DISCORD_BOT_TOKEN=<insert_token>
```
