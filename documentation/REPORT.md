#Demandes formulées : travail à effectuer

##Particularités à implémenter

###Technologies

* Utiliser la programmation orientée composants avec Maven. Manipuler les XML pour intégrer les ressources externes nécessaires.
* Utiliser le system de logger de log4j. Utiliser les ressources avec le dossier resources pour externaliser les paramètres du logger.
* Compiler avec Maven pour générer un jar embarquant les librairies nécessaires.

###Anomalies (Redmine Issues)

* Une anomalie ne contient pas forcément un historique de statuts, ce dernier peut alors être vu comme un attribut à valeur nulle. Il faut alors prévoir le cas pour ne pas faire face à des NullPointerException. Si la liste est vide, appeler une fonction retournant un objet RedmineIssue. Si elle n'est pas vide, appeler une fonction retournant l'objet redmine + le dernier Statut.

* Les différents statuts par anomalie sont regroupés dans une liste d'objets "Statuts". Un objet par statut. Chaque objet "Statut" contiendra les différentes caractéristiques de chaque statut.
* Le journal des anomalies par statut devra être trié dans l'ordre décroissant. L'utilisation et le parcours du Journal se fara via le design pattern iterator, à travers l'API.
* Il faudra placer les résultats des traitements effectués via l'API dans des objets RedmineIssue. L'objet redmineIssue contiendra lui même une liste Arraylist typé RedmineStatus, des objets qui correspondent chacun à une ligne d'une anomalie. Les différents membres de des objets seront affectés via des getters de l'API au moment de la construction de l'objet.
* Attention : le statut courant sur une Anomalie est différent du statut à la date de création. La date de création doit être associée au premier statut du journal, le plus vieux, et non au statut courant.
* Il ne faut récupérer pour une date donnée que le dernier statut. Il ne reste que la ligne du statut final pour chaque date différente.
* Si une anomalie contient un historique de statuts, il faut alors prévoir 2 cas, 2 comportements de récupération. Le premier consiste à récupérer uniquement le dernier statut par anomalie : il faut alors recourir à un TreeSet<> de référence SortedSet<> sur une liste d'objet RedmineStatus, car l'API par defaut n'effectue pas de Tri. Récupérer ensuite le dernier élément de la liste.
* le deuxième comportement consiste à récupérer l'ensemble du journal pour chaque anomalie avec uniquement le nouveau statut pour chaque ligne.
* Sortir également un fichier qui permette de repérer les anomalies qui ont eu un passage de statut de Intégré, à nouveau. Ce sont les anomalies réouvertes. Il va falloir créer un tampon (buffer) au moment du bouclage sur chaque objet Anomalie pour repérer les anomalies concernées. On pourra ainsi connaitre le nombre d'anomalies réouvertes à tel date. On écrit une ligne pour chaque Issue dans un autre fichier pour en avoir la comptabilisation.
* Les fichiers CSV contiendront pour chaque ligne : ID	SUJET	STATUT	CREE LE	VERSION DE CORRECTION	IMPACT	PROJET
* Une fois l'API maitrisée et les comportements définis, il faut réefectuer les mêmes traitemements pour d'autres projets en  cours dans l'entreprise.
* Utiliser obligatoirement un système de log avancé.

###Compilation, génération du programme jar

* Concevoir l'application pour qu'elle fonctionne à la fois sur Windows et sur Linux. Utiliser les librairies JAVA pour détecter le système sur lequel est le programme.
* Javadoc et commentaires obligatoires pour chaque méthode

