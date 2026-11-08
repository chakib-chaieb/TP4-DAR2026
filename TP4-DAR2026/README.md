# TP 4 : Sockets en mode non-connecte UDP

**Matiere** : Developpement d'applications reparties
**Responsable du cours** : Amal ABID
**Enseignante des TPs** : Zeineb DAOUD
**Etudiant** : Chaieb Chakib - LSI 3
**Faculte des Sciences de Sfax - Annee universitaire 2026 - 2027**

## Objectif

Realiser un service de chat client/serveur en Java avec le protocole UDP (`DatagramSocket` et `DatagramPacket`), d'abord en mode unidirectionnel avec un seul client, puis en mode bidirectionnel avec diffusion des messages a plusieurs clients.

## Environnement

- JDK 8 ou superieur
- IntelliJ IDEA
- Git

## Structure du projet

```
ChaiebChakib_LSI3_DevAppReparties
├── src
│   ├── etape2
│   │   ├── ServeurUDP.java
│   │   └── ClientUDP.java
│   └── etape3
│       ├── ServeurChatUDP.java
│       └── ClientChatUDP.java
├── captures
└── README.md
```

## Execution sous IntelliJ

1. Ouvrir le dossier du projet avec IntelliJ IDEA (`File > Open`).
2. Clic droit sur le dossier `src` puis `Mark Directory as > Sources Root`.
3. Lancer le serveur en executant sa methode `main`.
4. Pour lancer plusieurs clients : `Run > Edit Configurations`, selectionner la configuration du client puis `Modify options > Allow multiple instances`. Lancer ensuite le client plusieurs fois.

## Etape 2 : Chat unidirectionnel (un seul client)

### Principe

- `ServeurUDP` cree un `DatagramSocket` non lie, puis le lie au port **1234** avec `InetSocketAddress`. Il attend les datagrammes dans une boucle infinie avec `receive()` et affiche l'adresse IP, le port de l'expediteur et le contenu du message.
- `ClientUDP` demande un nom d'utilisateur, lit les messages au clavier et les envoie au serveur sous la forme `[NomUtilisateur] : message` avec `send()`.

### Execution

1. Lancer `etape2.ServeurUDP`.
2. Lancer `etape2.ClientUDP`, saisir un nom puis envoyer des messages.

### Captures d'ecran

![Etape 2 - Serveur](captures/etape2_serveur.png)

![Etape 2 - Client](captures/etape2_client.png)

### Synthese

Le client envoie chaque message dans un datagramme independant sans etablir de connexion prealable. Le serveur recoit les datagrammes, les affiche avec l'adresse (IP et port) de l'expediteur, mais ne repond pas : la communication est unidirectionnelle.

## Etape 3 : Chat bidirectionnel et diffusion a plusieurs clients

### Principe

- `ServeurChatUDP` conserve dans un `Set<SocketAddress>` les adresses de tous les clients ayant envoye un message. A chaque reception, il ajoute l'expediteur s'il est nouveau, puis retransmet le message a tous les clients enregistres sauf l'expediteur.
- `ClientChatUDP` utilise deux flux concurrents : le thread principal lit le clavier et envoie les messages, un thread secondaire attend en continu les datagrammes du serveur et les affiche en temps reel. Le client envoie un message `a rejoint le chat` au demarrage afin d'etre enregistre par le serveur des le debut. La commande `exit` ferme le client.

### Execution

1. Lancer `etape3.ServeurChatUDP`.
2. Lancer plusieurs instances de `etape3.ClientChatUDP` avec des noms differents.
3. Echanger des messages entre les clients.

### Captures d'ecran

![Etape 3 - Serveur](captures/etape3_serveur.png)

![Etape 3 - Client 1](captures/etape3_client1.png)

![Etape 3 - Client 2](captures/etape3_client2.png)

![Etape 3 - Client 3](captures/etape3_client3.png)

### Synthese

Le serveur joue le role de relais : UDP etant non connecte, la "connexion" d'un client est simulee par l'enregistrement de son adresse a la reception de son premier datagramme. Le message d'un client est diffuse a tous les autres sans lui etre renvoye. L'utilisation de threads cote client permet d'envoyer et de recevoir simultanement, ce qui donne un echange quasi temps reel. UDP ne garantit ni la livraison ni l'ordre des datagrammes, ce qui est acceptable pour un chat simple mais limite la fiabilite par rapport a TCP.

## Depot Git

```
git init
git add .
git commit -m "TP4 : chat UDP client/serveur"
git branch -M main
git remote add origin https://github.com/<utilisateur>/ChaiebChakib_LSI3_DevAppReparties.git
git push -u origin main
```
