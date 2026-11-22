export type Statut = 'EN_ATTENTE' | 'CONFIRME' | 'PRIS_EN_CHARGE' | 'EN_TRANSIT' | 'EN_COURS_DE_LIVRAISON' | 'LIVRE' | 'ECHEC_LIVRAISON' | 'RETOURNE' | 'ANNULE';
export interface Adresse { rue: string; ville: string; codePostal: string; }
export interface Client { id: string; nom: string; telephone: string; email: string; adresses: Adresse[]; }
export interface Livreur { id: string; nom: string; cin: string; telephone: string; typeVehicule: string; zone: string; disponible: boolean; nbColisEnCours: number; tauxReussite: number; }
export interface HistoriqueStatut { statut: Statut; date: string; commentaire: string; }
export interface Colis { id: string; trackingNumber: string; expediteur: Client; destinataire: Client; adresseCollecte: Adresse; adresseLivraison: Adresse; poids: number; dimensions: string; typeColis: string; tarif: number; statut: Statut; historiqueStatuts: HistoriqueStatut[]; livreurId?: string; paiement: { mode: 'COD' | 'CARTE' | 'VIREMENT'; statut: 'PAYE' | 'IMPAYE' | 'REMBOURSE' }; dateCreation: string; }
export interface Tournee { id: string; livreurId: string; date: string; colis: string[]; ordre: string[]; statut: string; bilan: { livres: number; echecs: number; retours: number }; }
export interface Incident { id: string; colisId: string; type: 'CLIENT_ABSENT' | 'ADRESSE_INTROUVABLE' | 'COLIS_ENDOMMAGE'; description: string; date: string; reprogrammationDate?: string; }
export interface Paiement { id: string; colisId: string; mode: 'COD' | 'CARTE' | 'VIREMENT'; statut: 'PAYE' | 'IMPAYE' | 'REMBOURSE'; montant: number; }
