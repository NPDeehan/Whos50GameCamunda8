# Whos 50 Game For Camunda Cloud


The game "Who's 50" is pretty simple - Name someone famous and if they are exactly 50 years old you win the game! If they're any other age or you can't find out what age they are you guess again.

I've turned this game into a Camunda Cloud application consisting of 3 parts. A BPMN 2.0 model, a couple of user forms and a Java microservice. 

## Prerequisites 

To use this project you need to setup some stuff, some you might already have, but you can follow the requirements list and you'll be all set.

### Camunda Cloud Account
Pop along to camunda.io and create a Camunda Cloud account. You'll need to create a cluster and setup client connection credentials. If you don't know how to do that, [just follow this tutorial](https://docs.camunda.io/docs/guides/getting-started/).

### Camunda Desktop Modeler (Optional)
You can [download it here](https://camunda.com/download/modeler/) for all the usual platforms. 

### Celebrity Ninja API Key
For this you just need to create a free account on [API Ninjas](https://api-ninjas.com/) and generate a key that you need to add to the `application.yml` file. Which is in the `src/main/resources/` 

```yaml
celebrityninjas:
  key: YourKeyGoesHere
```
## The Celeb Details Microservice

Most of this project is the micro-service that queries and api with the name of a celebrity and returns the age of that celebrity. 

You need to set it up by adding your Camunda Cloud API details to the `application.yaml`. If you don't know how to get the API details that are required just [follow these docs](https://docs.camunda.io/docs/guides/getting-started/setup-client-connection-credentials/).

Once you've added the API details you can start up the worker by running the `worker` class. 


## BPMN Model

![BPMNModel](./BPMNandForms/Whos50Game.png)

This is the executable process, that controls how the game will be played, the fastest way to deploy and start the process is:
1. Go to the `Diagrams` section of your camunda cloud account
1. Create a new diagram
1. Beside the `New Diagram`name there's a menu where you can upload a model.
1. Select the `Who's 50 Game` process from the `BPMNandForms` folder and it should appear.
1. Click the Execute button on the top left.

One you've deployed and started the instance you can go to your Camunda Cloud cluster and open up Tasklist.

From there you should see a user task asking you to add the name of someone.. so go do that.

Have fun! :) 