import petReducer from './PetReducer'
import adminReducer from './AdminReducer'
import {combineReducers} from "redux";
import signInReducer from "./SignInReducer";

const root_reducer = combineReducers({pets: petReducer, admins:adminReducer, signIn:signInReducer });

export default root_reducer
