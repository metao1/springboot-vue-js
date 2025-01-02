import { useStore as baseUseStore } from 'vuex'
import { Store } from 'vuex'
import { RootState } from './types'

export function useStore(): Store<RootState> {
    return baseUseStore<RootState>()
}
