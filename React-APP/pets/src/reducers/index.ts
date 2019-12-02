import petReducer from './PetReducer'
import adminReducer from './AdminReducer'
import {combineReducers} from "redux";

const root_reducer = combineReducers({pets: petReducer, admins:adminReducer });

export default root_reducer
