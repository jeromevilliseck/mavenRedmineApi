#Taipan mise en route rapide

##Utiliser un Job jenkins pour créer une branche

1. Aller dans Jenkins, rentrer ses identifiants, puis dans le fil d'ariane puis au dessus, se rendre dans BMS, puis creation-chantier-bms en cliquant sur les fleches du fil
1. Dans le panneau de gauche, faire Lancer un build avec des paramètres
1. Suivre les instructions en precisant le REPO et la BRANCH-NAME.
1. Utiliser gitBash pour se positionner sur la branche.

##[101] Créer un bundle | Compiler | Vérifier état d'un Bundle | Les fichiers XML

###Créer un nouveau module

1. Se positionner sur le dossier bms [fr.dga.sics.parent]
1. clic droit > new module
1. laisser java dans la barre de gauche du wizard qui apparait
1. next
1. ATTENTION: mettre le chemin entier à partir du bms, dans cet exemple

```
Name: [nom_de_votre_bundle]
Content root: C:\sics\repository\git\bms\app\[nom_de_votre_bundle]
File location: C:\sics\repository\git\bms\app\[nom_de_votre_bundle]
```

###Créer un fichier sonar project dans le bundle

1. Aller sur le Bundle
1. Clic droit > New > Ressource Bundle
1. dans le wizard mettre en nom sonar-project
1. un fichier sonar-project.properties va etre crée
1. Ouvrir le fichier sonar-project y mettre les lignes suivantes

```
#Project information
sonar.projectKey=fr.dga.sics:fr.dga.sics.[nom_de_votre_bundle]
sonar.projectName=[Nom_de_votre_bundle_de_maniere_libre_sans_les_points]
sonar.projectDescription=[Décrire votre Bundle]
sonar.sources=src/main/java
sonar.java.binaries=target/classes
```

###Initialiser Maven dans le Bundle

1. Enregistrer
1. Faire clic droit sur le dossier qui est le Bundle
1. Faire Add Framework Support
1. Dans le wizard qui apparait cocher Maven
1. Le Src va se doter des sous dossiers main et test, et du fichier pom.xml à configurer

####Notes

* Emplacement de maven par defaut
>C:\Outils\apache-maven

* Configuration de maven dans intellij
>File > Settings > Build, Execution, Deployment > Build Tools > Maven

###Gagner du temps a la compilation : Avoir des versions identiques dans les fichiers pom.xml du projet bms-sics-product et du projet bms

####Quel est la différence entre des deux dossiers et à quoi servent-ils

#####Il faut bien différencier

* le dossier **bms** qui représente le projet de départ. Générique il contient les sources dont dérivent tous les produits bms. *C'est à partir de ce dossier que l'on travaille toujours sur le code source*
* le dossier **bms-sics-product** qui représente le produit qui va être construit à partir du travail effectué dans le projet bms. Projet distinct, *C'est à partir de ce projet que l'on va compiler le produit*, en lancant la commande 
```
maven clean install
```
soit à partir de gitbash en se positionnant dans le dossier, soit à partir de intellij en haut a droite en faisant un 
```
run clean install
```
que l'on aura préalablement configuré. On dit que l'on Build sur le produit.

#####Il faut donc à l'ouverture d'intellij ouvrir 2 projets, un à partir du dossier bms, l'autre à partir du dossier bms-sics-product

1. Ouvrir le fichier **pom.xml** de plus haut niveau du dossier **bms-sics-product**
1. Reperer la balise **< bms.version>** (qui est une sous balise de la balise **< properties>** elle même sous balise de **< project>**).
   *Le contenu de cette balise va s'adapter au contenu de la balise version du fichier parent pom.xml du projet bms, pas l'inverse!*
1. Ouvrir le fichier **pom.xml** de plus haut niveau du dossier **bms**
1. Reperer la balise < version>, y placer le contenu correspondant à la branche que vous avez crée avec Jenkins, ex: **1.0.0.3-SNAPSHOT**, copier ce contenu
1. Retourner dans le pom.xml du projet bms-sics-parent et y coller le contenu copier dans la balise < bms.version>

_**Ensuite à la compilation le gain de temps sera de 20min environ avec des version identiques**_

####Utiliser le produit final

* Le produit sera compilé dans 
```
..\bms-sics-product\assemblies\runtime\target
```
Le dezipper avec **7-zip** uniquement, puis pour le lancer ouvrir 
```
sics[votre_version]\bin\sics-runtime.cmd
```

###Comprendre le découpage en sous-système : les différentes parties d'un fichier XML, rajouter son bundle dans le sous-système

Dans votre fichier **pom.xml**, modifier et reperer les groupes de balises suivant : parent indique le package parent ainsi que son fichier pom.xml associé

```xml
<parent>
        <groupId>fr.dga.sics</groupId>
        <artifactId>fr.dga.sics.parent</artifactId>
        <version>1.1.3-[votre_branche]-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
</parent>
```
artifactId, name et description des informations relatives a votre bundle
```xml
<artifactId>fr.dga.sics.[nom_du_bundle_en_chemin]
		<name>Le nom de votre bundle de mainiere libre
		<description>
<![CDATA[la description de a quoi sert le bundle]]>
</description>
```
La partie dependancies qui contient les differents bundle que l'on souhaite utiliser dans le bundle en cours (vu + tard)
```xml
<dependencies>
	< dependency>
        < groupId>${project.groupId}</groupId>
        < artifactId>fr.dga.sics.[le_bundle_dont_vous_aller_utiliser_des_services_dans_ce_bundle]</artifactId>
        < version>${project.version}</version>
    </dependency>
</dependencies>
```
La partie build qui contient les différents plugins qui sont necessaires pour faire fonctionner les fonctionnalités du bundle
```xml
<build>
        <plugins>
            <plugin>
                <groupId>biz.aQute.bnd</groupId>
                <artifactId>bnd-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>net.atos.taipan.tools</groupId>
                <artifactId>taipan-component-maven-plugin</artifactId>
            </plugin>
            <plugin>
            	..rajoutez les plugins qui vous sont nécessaires
            </plugin>
        </plugins>
</build>
```

###Comprendre le découpage en sous-système : rajouter le bundle crée dans le POM Parent, rajouter le Bundle dans le sous-système correpondant

####Rajouter le Bundle dans le POM parent en tant que module

Ouvrir le **pom.xml** du dossier bms, y rajouter la balise suivante

```xml
<module>app/fr.dga.sics.[nom_de_votre_bundle]</module>
```

####Rajouter le bundle dans le sous-systeme correspondant

Exemple de sous systeme : 
>bms > assemblies > subsystems > bms-67

* Ouvrir le fichier **pom.xml** du sous-système
* Y rajouter une balise dependancies avec le bundle

###Vérifier l'Etat du Bundle dans la console

####Créer une classe test (TP) dans le bundle recemment crée

1. Se rendre dans le bundle nouvellement crée
1. Aller dans src/main/java
1. Créer un package fr.dga.sics.[nom_de_votre_bundle]
1. Créer une classe test sans oublier les @ de la javadoc
1. Créer un package internal dans fr.dga.sics.[nom_de_votre_bundle] (sera utile par la suite)

####Bundle : "builder"

1. Clic droit sur le Bundle, build.
1. Cela va lancer la compilation, success normalement.
1. Retourner dans le projet **bms-sics-product**
1. Puis lancer la compilation via le bouton play en haut à droite (clean install), > compilation lancée 
   Mesure : si les sources sont différentes entre bms et bms-sics, comptez 20min
   
#####Autre facon de compiler les bundle individuellement

Faire dans intellij 
>View > Tools Windows > Maven Projects
1. un panneau avec tous les bundles s'affiche à droite 
1. Tapez directement au clavier pour faire des recherches, puis selectionner les bundles qui vous interesse avec les commandes voulu et faites **run**
1. Attention *Il n'est possible de faire les commandes que sur un bundle à la fois*

###Vérifier l'Etat du bundle dans la console

Se rendre dans 
>C:\sics\repository\git\bms-sics-product\assemblies\runtime\target
1. Dezipper le projet qui est sous forme zip avec 7zip uniquement
1. Se rendre dans le projet dezippé dans sics-.../bin puis lancer sics-runtime.cmd
1. Dans la commande cmd qui vient de s'ouvrir, une fois l'application SICS ouverte, la rebasuler au premier plan puis saisir list
   -> ceci va lister les bundles en cours de fonctionnement à chaud, leur etat peut être **active** (ok), **installed**, **grace period**, **failure**.
1. Tapez Help dans le cmd qui liste l'ensemble des commandes disponibles

_**Remarquez que le bundle qui à été crée n'est pas présent dans la console. C'est normal, car ce dernier doit être intégré comme dépendance**_

##[102] @Inject, @Service, différences | Notion de Contrat | dépendances nécessaires d'un Bundle

###Comprendre la notion large "d'interface", de contrat, le package internal contenant les implémentations

Dans un Bundle

1. **[Programmeur concepteur]** Le package internal contient les Classes (implémentations = attributs + méthodes dotées de leur corps)
1. **[Programmeur utilisateur]** Un niveau au dessus du package Internal, on place les interfaces qui sont implémentées par les classes
1. Le fichier **bnb.bnd** déclare ce que le bundle va exposer au reste du monde, se situant au premier niveau du bundle
1. Le **POM.xml** de mon bundle contient les différentes dépendances : autrement dit les bundle dont j'ai besoin pour mes input

####Exemple

Je crée une interface *CommunicationProfileService*

```java
package fr.dga.sics.monpremierbundle;

/**
 * @version 1.0
 * @since jdk 1.8
 * @author Bull/Atos
 */
public interface CommunicationProfileService {
    String getId();
    String getCommunicationType(boolean comType);
    String getName();
}
```

Je l'implémente dans un Service. Un _**Service**_ est une classe qui est placé dans le package internal, et qui implémente l'interface.

```java
package fr.dga.sics.monpremierbundle.internal;

/*Import de l'interface du bundle*/
import fr.dga.sics.monpremierbundle.CommunicationProfileService; 

/*Import d'un service externe (= une interface d'un autre Bundle) 
-> ne pas oublier les dependances du fichier XML*/
import fr.dga.sics.profile.ProfileService;

import net.atos.taipan.component.Inject;
import net.atos.taipan.component.Service;


@Service(interfaces = CommunicationProfileService.class)

/*JE FOURNIS L'UTILISATION DE MA CLASSE ENCAPSULEE DANS INTERNAL
(non visible de l'exterieur)
VIA L'INTERFACE CommunicationProfileService
(visible de l'exterieur)*/

public class HProfileServiceImpl implements CommunicationProfileService {
    
/*CommunicationProfileService est bien remontée d'un niveau par rapport a external
et est visible de l'extérieur
HProfileServiceImpl est dans le package internal 
et est non visible de l'exterieur
J'ai fourni un contrat : de l'exterieur (dans d'autres Bundles) 
j'utiliserai l'interface en manipulant des membres avec @inject
et donc l'implementation de l'exterieur je n'ai pas a la connaître, 
elle est dans la classe
*/

        @Inject
        private ProfileService profileService;
		
        /*Utilisation de l'interface Extérieure ProfileService à l'aide de Inject 
        + Déclaration dans le POM.XML
        du bundle qui contient l'interface : la classe ProfileServiceImpl est un 
        Service d'un autre Bundle.
        */
        
        @Inject
        private LanModeService lanmodeService; 
        
        /*autre membre interface*/

        public String getId(){
                return this.profileService.getMainUser().getParticipantIdExt();
        }
}

/*Utilisation du membre profileService dans la classe utilisatrice*/
```

Ensuite je vais utiliser la classe HProfileServiceImpl dans d'autres packages via @Inject

##[103] @Component ≠ @Service | @Start, @Stop | IHM

###Différence entre @Component, et @Service

####Utiliser un @Service

#####Fournir un service

Quand je fais...

```java
@Service(interfaces = [nom_de_ma_classe_interface].class)
public class MaClasse implements [nom_de_ma_classe_interface] {
```

...je suis en train de dire :
1. j'ai construit une interface
1. je construit une classe qui implémente mon interface, ça deviens donc un service
1. je rends ce service disponible pour l'extérieur, _**je fournis un service**_
1. c'est dans ce cas que j'utilise bnd.bnd en premier niveau de mon bundle qui contient 
```
Export-Package :fr.dga.sics.[nom_de_mon_bundle]
```

#####Utiliser un service

Quand je fais...

```java
@Inject
private [InterfaceExposee] membre;
```

...je déclare un membre. Ce membre est une interface exposée. Puis j'utilise les méthodes fournies par cette interface exposée

```java
this.interfaceExposee.methode1().methode2() //etc...
```

####Utiliser un component

#####Comprendre le système des components

1. Le paramètre configuration id contient en valeur un dossier, le dossier config du bundle : on dit que le component pointe vers ce dossier. Ce dossier contient un fichier cfg, ce fichier contient des paramètres.
1. _**Un component peut avoir ou ne pas avoir un paramètre, ce n'est pas obligatoire**_
1. Le fichier cfg contient des paramètres qui sont les attributs du fichier java qui possède la balise component : cela permet de donner une valeur par defaut aux atributs dans le fichier cfg et non plus dans la classe directement.
1. Ainsi on manipule les fichiers de configuration et non plus les classes en direct pour les valeurs de certains attributs.

#####Exemple d'utilisation d'un component

```java
/*le parametre configurationId qui pointe vers un fichier : 
Le bundle contient un dossier config avec un fichier fr.dga.sics.verticalview.cfg*/
@Component(configurationId = "fr.dga.sics.verticalview")
public class VVThematicControllerFactoryImpl extends VerticalViewControllerFactory implements
VVThematicControllerFactory, SelectionListener {

/*L'attribut contient une valeur par defaut dans le fichier cfg*/
protected int verticalViewOffset; 
```

**Un component peut avoir ou ne pas avoir de paramètre, ce n'est pas obligatoire.**

Fichier _*fr.dga.sics.verticalview.cfg*_ du bundle situé dans le dossier _*fr.dga.sics.verticalview*_ : ce fichier va contenir toutes les valeurs des attributs

```java
/*On donne ainsi toutes les valeurs par defaut des attributs à travers les fichiers de configuration*/
#Size of area around the vertical view line used to detect the points objects (in meters)
verticalViewOffset = 50 

#Step size between tow points use to calculate elevation
stepSize = 30
```

Ainsi on manipulera ulterieurement les fichiers de configuration et non plus les classes pour attribut des valeurs par défaut aux attributs

###Annotations @Start et @Stop

@Start permet de lancer une méthode d'un service obligatoirement au lancement d'un Bundle. Un Bundle étant normalement chargé au démarrage, une méthode portant l'annotation Start sera lancée automatiquement.

####Procédure pour utiliser @Start

* Importer dans le fichier XML du bundle la dépendance nécessaire, c'est component

```xml
<dependency>
<groupId>net.atos.taipan.component</groupId>
<artifactId>net.atos.taipan.component</artifactId>
</dependency>
```

On peut remarquer à la syntaxe du component que c'est un component du framework et non un component du produit.

* Ecrire la méthode onStart (c'est ne nom qu'on donne par convention) dans la classe (exemple ici qui permet de visualiser le menu grace a la classe ItemAction de sics basée sur Swing)

```java
package fr.dga.sics.monpremierbundle.ui;

import fr.dga.sics.core.ui.swing.menu.ItemAction;
import fr.dga.sics.core.ui.swing.menu.MenuManager;

import fr.dga.sics.monpremierbundle.ui.internal.ProfileAction;
import fr.dga.sics.ui.MainPanelViewProvider;

/*ne pas oublier dans le pom.xml la dependance necessaire*/
import net.atos.taipan.component.Inject; 
import net.atos.taipan.component.Start;
import net.atos.taipan.component.Stop;

@Component /*On voit ici un component n'ayant pas de paramètres*/
public class ProfilController {

@Inject
    private MenuManager menuManager;

@Inject
    private ProfileAction profilAction;

@Start
    public void onStart() {

        item = new ItemAction.Builder<>((m,v) -> {profilAction.openPopup();})
                .simpleButton()
                .label("WhoIAm")
                .icon(LAFResource.validate.getIcon())
                .build();

        show();
    }

    public void show() {
        // Add it to root menu
        menuManager.bind(MainPanelViewProvider.PLACEHOLDER_MENU, item, 500);
    }
}
```

* Lancer SICS, puis dans le cmd, faire un list. Noter le numéro du bundle ou est écrite la méthode avec l'annotation start. Le Bundle étant déja lancé au démarrage même sans l'annotation, voir l'étape suivante avec l'annotation @Stop.

####Procédure pour utiliser @Stop

1. Même procédure que pour @Start, il faut intégrer le component @Stop au bundle
1. Ensuite, il faut ajouter dans la classe qui implémente l'annotation

```java
    @Stop
    public void onStop() {
        hide();
    }

    public void hide() {
        // Remove it from root menu
        menuManager.unbind(MainPanelViewProvider.PLACEHOLDER_MENU, item);
    }
        
```

_**Vous pouvez générer automatiquement les imports nécessaires (en classes d'autres bundles, à la condition de bien avoir déclaré les dépendances) en faisant ALT + ENTREE**_

* Lancer SICS, puis faites list, bundle:stop [numero_du_bundle] pour masquer le menu.

####Annotation @Start : résumé, bonne conception

```java
 /*Ecrire par convention la méthode injectant start, onStart
+ retour de type VOID obligatoire
+ faire un this.getMethode pour faire cible le start sur une méthode externe au start
*/    
                @Start
                public void onStart(){
					this.getMethode();
				}
				
				public ... getMethode(){
					//...ecrire le corps
				}
```

###IHM

####Commencer à manipuler l'IHM

#####Créer une classe ProfileAction qui étend AbstractAction, et qui affiche un Popup grace au service DialogHelperInjector

Bien entendu, les imports necessaires et dépendances doivent être résolues avant l'écriture de la classe.

```java
import fr.dga.sics.monpremierbundle.CommunicationProfileService;
import fr.dga.sics.ui.dialog.DialogHelperInjector;
import fr.dga.sics.ui.action.AbstractAction;

@Component(configurationId = "fr.dga.sics.monpremierbundle.ui")
public class ProfileAction extends AbstractAction {

	@Inject
    private DialogHelperInjector dialogHelperInjector;

	@Inject
    private CommunicationProfileService communicationProfileService;

        public void displayDialog(){

		/*Construction d'un objet de type StringBuilder*/
        StringBuilder objet = new StringBuilder();

		/*Recuperation des éléments de communicationProfileService*/
        objet.append(communicationProfileService.getName());

		/*Afficher une Popup qui affiche les informations fournies 
		par le service injecté communicationProfileService*/
        this.dialogHelperInjector.getDialogHelper().createInformationDialog(null,
                new ConfirmView(objet.toString()), 
                new Dimension(200, 300)).setVisible(true);
        }
}
```

##[104] Utiliser les Resources

Les ressources permettent _**d'externaliser un chaine de caractères**_ : c'est à dire que ce n'est plus dans les classes que l'on effectue une affectation : on va déclarer la variable, puis déclarer la valeur de cette dernière dans un fichier à part.

###Méthodologie

1. Créer dans un bundle un dossier `taipan > ressources > values` et y creer un fichier en faisant `clic droit sur le dossier > new > Resource Bundle`. Le nommer strings. Le fichier `strings.properties` apparaît.

1. Ouvrir le fichier crée, et par exemple pour une variable label_participant qui serait présente dans ma classe, y mettre les lignes suivantes

```java
label_participant=Je suis le participant {0} et je possède {1} élément					
```

L'identifiant du service sera affiché a la place du paramètre 0.

**Attention ! Lorsque l'on récupère du contenu de type String pour prendre la place de 0, 1, 2, etc..., il vaut mieux utiliser les %s à la place des {0}, {1}, etc... car cela est source d'erreurs.**

1. Ouvrir le pom.xml du Bundle. Y mettre les balises suivantes :

```java
<build>
	/*..Balise dependancy*/
	<dependency>
		<groupId>net.atos.taipan.resources</groupId>
		<artifactId>net.atos.taipan.resources</artifactId>
	</dependency>

	/*..Balise plugin*/

	<plugins>
			<plugin>
                <groupId>net.atos.taipan.tools</groupId>
                <artifactId>taipan-resources-maven-plugin</artifactId>
            </plugin>
	</plugins>
</build>			
```

1. Dans la classe ou l'on veut utiliser les variables du fichier strings.properties

```java

	/*...*/

	/*Import obligatoire (faire ALT+ENTREE au moment ou l'on écrit le 
	@Inject private Resources resources)*/
import net.atos.taipan.resources.Resources;

	/*Injection du service permettant d'utiliser les ressources*/
@Inject
private Resources resources;

	/*Utilisation du service Resources pour générer un affichage basé 
	sur le fichier Strings.properties
    
    Je crée un Objet par exemple de type StringBuilder que je fournis 
    en caractères*/
StringBuilder objet = new StringBuilder();
objet.append(resources.getString(R.label_participant, objetFictif.getParticipantNumber(), objetElementsFictif.getSize()));
	/*COMPRENDRE
	Dans la méthode getString(), le premier paramètre permet à l'aide de la 
	Classe R.[nom_variable_fichier_strings.properties] d'afficher la valeur 
	de la variable
	
	Le deuxième paramètre (qui peut être une méthode, une valeur, etc) va 
	aller prendre la place du {0} dans la chaine du fichier strings.properties
	
	Le troisième paramètre va aller prendre la place du {1} dans la chaîne, 
	etc...
	*/
						
```

Attention ! la methode ressources.getString(...) ne doit contenir que des methodes qui retournent des strings.

##[105] Ajouter et utiliser un paramètre en conf avec l'annotation @Configuration

Il faut **différencier les strings.properties, des CFG de configuration**. Les fichiers strings.properties, sont utilisables avec ressources.methode, avec les .cfg on va externaliser les valeurs par defaut données aux attributs dans les classes.

1. Dans le package `src > main` de votre bundle, créer un dossier config, puis y créer un fichier `fr.dga.sics.[mon_bundle].cfg`
1. Dans le fichier `.cfg`, mettre la ligne suivante

```java
attributTest=valeur de mon attribut test dans le fichier de configuration
```

Vous avez crée une valeur à un attribut.
Dans le pom.xml du bundle, rajouter les dépendances suivantes si elle ne sont pas deja présentes.

```java
/*La dépendance*/

<dependency>
            <groupId>net.atos.taipan.component</groupId>
            <artifactId>net.atos.taipan.component</artifactId>
</dependency>

/*Le plugin*/

<plugin>
                <groupId>net.atos.taipan.tools</groupId>
                <artifactId>taipan-component-maven-plugin</artifactId>
</plugin>

/*Le plugin Build helper Maven Plugin*/

<plugin>
      <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>attach-artifact</goal>
                        </goals>
                        <configuration>
                            <artifacts>
                                <artifact>
                                    <file>${project.basedir}/src/main/config/fr.dga.sics.monpremierbundle.cfg</file>
                                    <type>cfg</type>
                                </artifact>
                            </artifacts>
                        </configuration>
                </execution>
      </executions>
</plugin>
```

Dans le fichier HProfileServiceImpl mettre les données suivantes

```java
/*Les imports*/
import net.atos.taipan.component.Configuration;

/*L'annotation component avec en parametre le configurationId qui pointe vers le bundle en cours*/
@Component(configurationId = "fr.dga.sics.[nom_de_votre_bundle_ou_est_la_config]")

/*Annotation necessaire sur chaque attribut utilisant des valeurs définies dans des fichiers de config*/
@Configuration 
/*La valeur par defaut est exportée dans le fichier cfg*/
private String name; 
```

##[200] Comprendre les dépendances cycliques

1. `Package monpremierbundle.ui` : Transformer ProfileAction en Service (+ créer une interface ProfileActionService dans le meme package)
```java
//...
@Service(interfaces = ProfileAction.class)
@Component(configurationId = "fr.dga.sics.monpremierbundle.ui")
public class ProfileAction extends AbstractAction {
//...
```

1. `Package mon premierbundle.ui` : l'interface expose une methode getActiveCommunication(). Dans la classe ProfileAction qui l'implémente, retourner la valeur getName du communicationProfileService (qui est un service d'un autre bundle, mon premier bundle)

```java
//@Inject
//private CommunicationProfileService communicationProfileService;
/*Utilisation de l'interface CommunicationProfileService via
l'injection du service qui est dans le bundle fr.dga.sics.monpremierbundle*/

public String getActiveCommunication(){
    return communicationProfileService.getName();
}
```

1. `Package monpremierbundle.ui` : Injecter l'interface de ProfileAction (qui est deja dans monpremierbundle.ui) est ajouter une methode isActive() qui compare la valeur de getActiveCommunication à un membre interne.

```java
//Injection de l'interface ProfileAction : constater qui le bundle A utilise des services du B, et le B des services du A
@Inject
private ProfileAction profileAction;

public boolean isActive(){
      if(profileAction.getActiveCommunication().equals(this.name))
            return true;
}
```

A ce stade les fichiers pom.xml des 2 bundles contiennent des dépendances croisées. Le A à une balise dependancy qui pointe vers B. Le B a une balise dependancy qui pointe vers A. C'est ce que l'on appelle des dépendances cycliques : le serpent se mord la queue : chacun attends des ressources de l'autre pour fonctionner.

###Corriger le problème

Utiliser un tiers qui fonctionne et qui va apporter les resources a A vers B et reciproquement.

##[201] Gérer les dépendances entre bundle : ajouter un bundle dans un sous-système

L'objectif est d'ici d'implementer dans une classe d'un bundle A une interface d'un bundle B, l'interface du bundle A contiendra les signatures des methodes, la classe du bundle B contiendra la definition de ces methodes.

On injecte également un Service provenant d'un Bundle natif de SICS.

* Intégrer dans le POM.XML de ce bundle la balise dependancy pour les bundles monpremierbundle et profile.

* Réaliser les imports nécessaires en début de classe

```java
import fr.dga.sics.monpremierbundle.CommunicationProfileService;
import fr.dga.sics.profile.ProfileService;
					
import net.atos.taipan.component.Inject;
import net.atos.taipan.component.Service;
```

* Fournir le contrat

```java
@Service(interfaces = CommunicationProfileService.class)
public class NC1ProfileServiceImpl implements CommunicationProfileService{
```

* Injecter le service du Bundle SICS

```java
@Inject
private ProfileService profileService;
```

* Utiliser le service profile, pour définir les méthodes de la classe, qui deviendra elle même un service à travers l'injection de son interface CommunicationProfileService, interface qui n'est toutefois pas situé dans le même bundle.

```java
@Override
    public String getId() {
        return profileService.getMainUser().getTheaterAddress();
    }

    @Override
    public String getCommunicationType(boolean comType){
        return "vous etes sur le NC1ProfileService";
    }
```

##[202] Utiliser un @Inject avec une List< InterfaceService> et pouvoir l'utiliser

