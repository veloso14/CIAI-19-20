import {Action} from "redux"
import {
    AddAdminAction,
    AdminActionsTypes,
    DeleteAdminAction,
    ReceiveAdminAction,
    ReceiveAdminsAction,
    UpdateAdminAction,
} from "../actions/AdminActions";
import {AdminState} from "../components/AdminList";

const initialState = {
    admins: [],
    admin: {
        id: -1,
        name: "",
        employeeID: -1,
        photo: "",
        address: "",
        password: "",
        username: "",
        cellphone: -1,
        email: ""
    },
    isFetching: false,
};

function adminReducer(state: AdminState = initialState, action: Action): AdminState {
    switch (action.type) {
        case AdminActionsTypes.ADD_ADMIN:
            return {
                ...state,
                admins: [...state.admins, {
                    id: 0,
                    name: (action as AddAdminAction).name,
                    employeeID: (action as AddAdminAction).employeeID,
                    photo: (action as AddAdminAction).photo,
                    address: (action as AddAdminAction).address,
                    password: (action as AddAdminAction).password,
                    username: (action as AddAdminAction).username,
                    cellphone: (action as AddAdminAction).cellphone,
                    email: (action as AddAdminAction).email,
                }]
            };
        case AdminActionsTypes.UPDATE_ADMIN:
            return {
                ...state,
                admin: (action as UpdateAdminAction).data.admin
            };

        case AdminActionsTypes.REQUEST_ADMINS:
            return {...state, isFetching: true};
        case AdminActionsTypes.RECEIVE_ADMINS:
            return {...state, isFetching: false, admins: (action as ReceiveAdminsAction).data};


        case AdminActionsTypes.REQUEST_ADMIN:
            return {...state, isFetching: true};
        case AdminActionsTypes.RECEIVE_ADMIN:
            return {
                ...state,
                isFetching: false,
                admin: (action as ReceiveAdminAction).data
            };


        case AdminActionsTypes.DELETE_ADMIN:
            return {...state, admins: state.admins.filter(admin => admin.id !== (action as DeleteAdminAction).id)};
        default:
            return state
    }
}

export default adminReducer