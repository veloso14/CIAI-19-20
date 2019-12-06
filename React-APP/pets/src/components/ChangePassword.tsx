import React, {useEffect, useState} from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {updatePasswordRequest} from "../actions/ChangePassword";
import {GlobalState} from "../App";
import Container from "react-bootstrap/Container";
import {Client} from "../pages/ClientPage";
import {getData} from "../Utils/NetworkUtils";


type FormData = {
    oldPassword: string;
    newPassord: string;
}



const ProtoUpdatePassword = (props: {currentUser: string, currentRole: string,updatePassword: (id: number, password: string) => void }) => {
    const clientID = 4;
    const [update, setUpdate] = React.useState(false);
    const [client, setClient] = useState({} as Client);
    const [loading, setLoading] = useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({newPassord}) => {
        console.log(newPassord);
        props.updatePassword(client.id, newPassord);
        setUpdate(true);
        setValue("oldPassword", "");
        setValue("newPassord", "");
    });



    const loadClient = (username: string) => {
        setLoading(true);
        return getData(`/admins/admin/${username}`, {} as Client)
            .then(data => {
                console.log("client loaded: " + JSON.stringify(data));
                setClient(data);
                setLoading(false);
            })
    };

    useEffect(() => {
        loadClient(props.currentUser)
        console.log(props.currentRole)
    }, []);


    return (
        <Container>

            <br/>

            <h1 className="text-center">Change Password</h1>

            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Old Password</label>
                    <input className="form-control" id="oldPassword" type="password" name="oldPassword"
                           ref={register({required: true})}/>
                    {errors.petName && 'Old Password is required'}
                </div>

                <div className="form-group">
                    <label>New Password</label>
                    <input className="form-control" id="newPassord" type="password" name="newPassord"
                           ref={register({required: true})}/>
                    {errors.petName && 'Passwordis required'}
                </div>

                <input className="btn btn-primary float-right" type="submit" value="Change"/>
            </form>
        </Container>
    );
};

const mapStateToProps = (state: GlobalState) => ({
    currentUser: state.signIn.currentUser,
    currentRole: state.signIn.currentRole
});

const mapDispatchToProps = (dispatch: any) => {
    return {
        updatePassword: (id: number, password: string) => {
            dispatch(updatePasswordRequest(id, password))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoUpdatePassword);