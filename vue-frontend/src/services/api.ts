import { Activity } from '../types/activity'

const API_BASE_URL = 'http://localhost:8080/api'

export async function fetchActivities(searchQuery: string): Promise<Activity[]> {
    try {
        const response = await fetch(`${API_BASE_URL}/activities?title=${encodeURIComponent(searchQuery)}`)

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
        }

        return await response.json()
    } catch (error) {
        console.error('API Error:', error)
        throw error
    }
}
