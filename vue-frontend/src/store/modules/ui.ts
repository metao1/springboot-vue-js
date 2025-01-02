import{ Module } from 'vuex'
import{ UIState, RootState } from '../types'

const ui: Module<UIState, RootState> = {
    namespaced: true,

    state: () => ({
        loading: false,
        error: null
    }),

    getters: {
        isLoading: (state) => state.loading,
        getError: (state) => state.error
    },

    mutations: {
        SET_LOADING(state, loading) {
            state.loading = loading
        },
        SET_ERROR(state, error) {
            state.error = error
        }
    },

    actions: {
        setLoading({ commit }, loading) {
            commit('SET_LOADING', loading)
        },
        setError({ commit }, error) {
            commit('SET_ERROR', error)
        },
        clearError({ commit }) {
            commit('SET_ERROR', null)
        }
    }
}

export default ui
