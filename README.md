## Package Debian
### Installer sudo
```shell
apt upgrade
apt update
apt install sudo
```

### Ouvrir le port 22 pour la connexion SSH
#### Autorisations dans .conf
Veuillez vérifier deux fichiers configuration du service SSH.4
```shell
sudo nano /etc/ssh/sshd_config
sudo nano /etc/ssh/ssh_config
```
Il faut décommenter les commandes suivant et les mettre sur "yes";
- PermitRootLogin yes
- Port 22 
- AuthentificationPassoword yes
##### Lancement
```shell
sudo systemctl start ssh

sudo systemctl status ssh
```
## Serveur Mère
Voici tous les éléments nécessaires à installer sur le serveur mère.
### Jenkins
#### JDK
Il faut tout d'abord installer jdk pour pouvoir installer Jenkins.
Voici les étapes:
```bash
sudo apt install openjdk-11-jdk
```
Regarder si tout est ok:
```shell
java -version
```
#### Jenkins
##### Installation
Pour l'installation de Jenkins, voici les commandes à saisir:
```shell
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian/jenkins.io-2023.key

echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt-get update

sudo apt-get install jenkins
```
##### Lancement
Pour lancer Jenkins:
```shell
sudo systemctl start jenkins
```

Et ci-dessous, le lien pour accéder à Jenkins: 
http://localhost:8080

Une premier mot de passe sera demander pour l'utilisateur admin.
Voici le fichier pour y accèder:
```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

##### Configuration
**Configurer les différents agents:**
Une fois connecter à Jenkins, veuillez aller dans _Administer Jenkins_ => _Nodes_ => _New Nodes_
Saisissez un nom puis sélectionnez _Permenant Agent_.

Ajouter les options nécessaire mais il est important de remplir les informations suivantes:
- Etiquettes
- Méthode de lancement (auxquelles il faut sélectionner _Lauch agents via SSH_)
- Saisir les informations de connexion SSH qui suivent ce choix. Ajouter des identifiants si nécessaire.
- Dossier du build : _/home/jenkins_

Sauvegarder votre noeuds et vérifier que la connexion c'est bien réalisé.

**Plugin**
Veuillez vous rendre dans _Administer Jenkins_ => _Plugin_
Installer les deux plugins suivants:
- [Parameterized Trigger plugin](https://plugins.jenkins.io/parameterized-trigger)
- [Node and Label parameter plugin](https://plugins.jenkins.io/nodelabelparameter)

Redémarrez ensuite Jenkins.

**Build**
- Créez un nouveau job dans Jenkins avec le bouton _Nouveau Item_.
- Saisissez un nom et choisissez _Projet Freestyle_.
- Sélectionnez ensuite l'option _Ce build a des paramètres_ et sélectionnez les nodes ou vous voulez que ce lance le build.
- Sélectionnez l'option _Disallow multi node selection when triggering build manually_
- Cochez ensuite ensuite l'option _Exécuter des builds simultanément si nécessaire_ 
- Cochez la case _Construire périodiquement_ et saisissez le code suivant pour lancer le build toutes les 5 minutes:
```shell
H/5 * * * * 
```
- A la fin de la page, sélectionnez _Ajouter un build_ => _Executer un script shell_
	Voici ce qu'il faut saisir dans le script:
```shell
#mise en place du dossier
cd /home/jenkins/<NomDuProjet>
#Mise à jour du code
git pull
#lancement du code avec MVN
mvn gatling:test -DGRAPHITE_PORT=2003 -DGRAPHITE_HOST=192.168.1.99 -DSCRIPT_NAME=NomDuProjet
```
### Grafana
#### Installation
Voici le commande pour installer Grafana:
```shell
echo "deb https://packages.grafana.com/oss/deb stable main" | sudo tee /etc/apt/sources.list.d/grafana.list

wget -q -O - https://packages.grafana.com/gpg.key | sudo apt-key add -
sudo apt update

sudo apt install grafana
```
#### Lancement
```shell
sudo systemctl start grafana-server
```
#### 1er connexion
Lien du site:
- http://localhost:3000
Lors de la première connexion vous devez saisir les identifiants suivants:
User: admin
Password: Admin
#### Configuration de la BDD InfluxDB
Une fois connecter dans Grafana, cliquez sur _Add new data source_ et sélectionnez InfluxDB.
Voici les informations à saisir:
- URL: http://localhost:< PORT > (le port dépendra de l'instance de celui-ci)
- database: < DATABSENAME > (dépendra de l'instance)
Faîtes ensuite _Save & Test_ et vérifier que la connexion est établie.

### InfluDB
#### Installation
Voici les commandes pour installer la première instance d'influxdb:
```shell
sudo nano /etc/apt/sources.list.d/influxdb.list
# dans le fichier: deb https://repos.influxdata.com/ubuntu focal stable

sudo apt update
sudo apt install influxdb
```
#### Configuration
Dans le fichier influx.conf, veuillez activer le paramètre gatling à la fin du fichier.
Comme ceci:
```.conf
[[graphite]]
	enabled = true
	database = "graphite"
	rentention-policy=""
	bind-adress=":2003"
	protocol = "tcp"
	consistency-level = "one"
```
#### ajouter une instance infludb
**Copier la configuration existante**
```shell
sudo cp /etc/influxdb/influxdb.conf /etc/influxdb/influxdb2.conf
```
**Éditer la nouvelle configuration**
```shell
sudo nano /etc/influxdb/influxdb2.conf
```
Changer ensuite tous les ports nécessaires pour éviter les confusion:
- graphite
- http
- bind-address (au début du fichier)
Créer ensuite les dossier nécessaire avec les autorisations:
```bash
sudo mkdir /var/lib/influxdb2
sudo chown influxdb:influxdb /var/lib/influxdb2
```
**Éditer le service**
```shell
sudo nano /etc/systemd/system/influxdb2.service
```

Voici la configuration:
```conf
[Unit]
Description=InfluxDB2
After=network.target

[Service]
User=influxdb
Group=influxdb
ExecStart=/usr/bin/influxd -config /etc/influxdb/influxdb2.conf
Restart=on-failure
RestartSec=5s

[Install]
WantedBy=default.target
```

## Raspberry Client
Il faut installer quelque package pour que tous se lance correctement.
### JDK
Il faut tout d'abord installer jdk pour pouvoir installer Maven.
Voici les étapes:
```bash
sudo apt install openjdk-11-jdk
```
Regarder si tout est ok:
```shell
java -version
```
### Maven
```shell
sudo apt update
sudo apt upgrade
sudo apt install maven
```

Regarder si tout est ok:
```shell
mvn -version
```
### Git
```shell
sudo apt update
sudo apt upgrade
sudo apt install git
```
### Configuration du dossier gatling
Voici les commandes pour réaliser le dossier gatling
```shell
cd ./home
sudo mkdir jenkins
cd ./jenkins
git clone <urlGit>
```
