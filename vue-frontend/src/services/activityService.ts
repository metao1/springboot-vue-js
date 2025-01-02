import { fetchActivities } from './api'
import{ Activity } from '../types/activity'

export const searchActivities = async (query: string): Promise<Activity[]> => {
    return await fetchActivities(query)
}
