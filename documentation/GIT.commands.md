#Git commandes du projet

##Pousser du code de la branche maintenance vers la branche master

###Etape 1

```bash
git checkout -b [cible] [origine]
```
On mets dans un tampon les modifications que l'on à effectué. Cette commande permet de créer une
nouvelle branche qui sert de tampon.

###Etape 2

```bash
git fetch [origine/branche serveur]
```
On récupère les dernières modifications du projet depuis le serveur.

###Etape 3

```bash
git checkout -B [cible] [origin/branche]
```

On ecrase la branche locale avec la branche à jour du serveur.

###Etape 4

Effectuer en GUI un cherryPick dans Intellij sur chaque commit. C-a-d que l'on copie les différents états vers la branche temporaire que l'on avait crée.

###Etape 5

```bash
git push [...]
```

Une fois le contrôle effectué, on push.


