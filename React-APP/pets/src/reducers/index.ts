import petReducer from './PetReducer'
import adminReducer from './AdminReducer'
import {combineReducers} from "redux";
import signInReducer from "./SignInReducer";
import vetReducer from "./VetReducer";

const root_reducer = combineReducers({pets: petReducer, admins:adminReducer, signIn:signInReducer,vets:vetReducer });

export default root_reducer
