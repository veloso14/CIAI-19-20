import petReducer from './PetReducer'
import {combineReducers} from "redux";

const root_reducer = combineReducers({pets: petReducer });

export default root_reducer
