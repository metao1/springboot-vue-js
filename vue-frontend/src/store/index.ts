import { createStore } from 'vuex'
import{ RootState } from './types'
import activities from './modules/activities'
import ui from './modules/ui'

export default createStore<RootState>({
    modules: {
        activities,
        ui
    }
})
