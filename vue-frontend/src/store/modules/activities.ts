import{ Module } from 'vuex'
import{ ActivitiesState, RootState } from '../types'
import { searchActivities } from '../../services/activityService'

const activities: Module<ActivitiesState, RootState> = {
    namespaced: true,

    state: () => ({
        items: [],
        searchQuery: ''
    }),

    getters: {
        getActivities: (state) => state.items,
        getSearchQuery: (state) => state.searchQuery
    },

    mutations: {
        SET_ACTIVITIES(state, activities) {
            state.items = activities
        },
        SET_SEARCH_QUERY(state, query) {
            state.searchQuery = query
        }
    },

    actions: {
        async searchActivities({ commit, dispatch }, query) {
            commit('SET_SEARCH_QUERY', query)
            dispatch('ui/setLoading', true, { root: true })
            dispatch('ui/clearError', null, { root: true })

            try {
                const activities = await searchActivities(query)
                commit('SET_ACTIVITIES', activities)
            } catch (error) {
                dispatch('ui/setError', 'Failed to fetch activities. Please try again later.', { root: true })
                commit('SET_ACTIVITIES', [])
            } finally {
                dispatch('ui/setLoading', false, { root: true })
            }
        }
    }
}

export default activities
