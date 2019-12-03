import React from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {updatePassword, updatePasswordRequest} from "../actions/ChangePassword";
import {GlobalState} from "../App";
import Container from "react-bootstrap/Container";



type FormData = {
    oldPassword : string;
    newPassord: string;
}


const clientID = 568

const ProtoUpdatePassword = (props: {  updatePassword: (id: number, password: string)=> void}) => {
    const [update, setUpdate] = React.useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({newPassord}) => {
        console.log(newPassord);
        props.updatePassword(clientID,newPassord);
        setUpdate(true);
        setValue("oldPassword", "");
        setValue("newPassord", "");
    });




    return (
        <Container>

            <br/>

            <h1 className="text-center">Change Password</h1>

            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Old Password</label>
                    <input className="form-control" id="oldPassword" type="password" name="oldPassword" ref={register({required: true})}/>
                    {errors.petName && 'Old Password is required'}
                </div>

                <div className="form-group">
                    <label>New Password</label>
                    <input className="form-control" id="newPassord" type="password" name="newPassord" ref={register({required: true})}/>
                    {errors.petName && 'Passwordis required'}
                </div>

                <input className="btn btn-primary float-right" type="submit" value="Change"/>
            </form>
        </Container>
    );
};

const mapStateToProps = (state: GlobalState) => ({

});

const mapDispatchToProps = (dispatch: any) => {
    return {
        updatePassword: (id: number, password: string) => {
            dispatch(updatePasswordRequest( clientID ,password))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoUpdatePassword);