import{ Activity } from '../types/activity'

export interface RootState {
    activities: ActivitiesState;
    ui: UIState;
}

export interface ActivitiesState {
    items: Activity[];
    searchQuery: string;
}

export interface UIState {
    loading: boolean;
    error: string | null;
}
