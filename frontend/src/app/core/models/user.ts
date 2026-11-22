export type UserRole = 'ADMIN' | 'GESTIONNAIRE' | 'LIVREUR' | 'CLIENT';
export interface User { id: string; nom: string; email: string; role: UserRole; }
