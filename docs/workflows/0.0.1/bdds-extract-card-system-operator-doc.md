## Enchainement des steps

Première étape :

- extrait les objets cartesPapier qui n'ont pas encore été édités
- constitue un fichier json avec les objets collectés
- convertit le fichier json en fichier xml (si le paramètre xmlConversion à true)
- dépose le fichier sur le workdir

Deuxième étape :

- déplace le fichier du workdir vers la PEFB
- produit le fichier .meta

Troisième étape :

- génère les tracesExtractionConso

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur la PEFB**

/INSTANCE_PEFB_PATH/PEFBOrganization/PEFBDirectory

- INSTANCE_PEFB_PATH : variable d'environnement instanciée dans la configMap (avec /workdir/Instance/Client)
- PEFBOrganization : paramétrage Assureur (ou par défaut le paramétrage Editeur) dans le fichier de conf S3. Par exemple : BOO/EG/PG dans le paramétrage Editeur
- PEFBDirectory : paramétrage Editeur dans le fichier de conf S3 : "BDD/Output"

Les paramétrages Editeur et Assureur sont récupérés avec le type de document "CARTES_PAPIER".

## Fonctionnement technique

**Paramétrage d'appel**

- La date d'effet de l'OMU (au format SSAA-MM-JJ) : --effectiveDate
- Le booléen qui indique si le fichier produit doit être au format xml : --xmlConversion

**Paramétrage d'environnement :**

- La racine du chemin qui va être utilisé pour générer les fichiers : INSTANCE_PEFB_PATH
- Le chemin du fichier de paramétrage Assureur S3 : insurer_settings_file_path