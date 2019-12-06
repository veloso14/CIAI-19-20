import React from 'react';
import {GlobalState} from "../App";
import {connect} from "react-redux";
import {useParams} from "react-router-dom"
import useForm from "react-hook-form";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image"
import {Admin} from "./AdminList";
import {fetchAdmin, updateAdminRequest} from "../actions/AdminActions";

type FormData = {
    newEmail: string;
    newCellphone: number;
    newAddress: string;
}


const AdminDetails = (props: { admin: Admin, loadAdmin: (id: string) => void, updateAdmin: (id: string, admin: Admin) => void }) => {
    let {id} = useParams();

    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({newEmail, newCellphone, newAddress}) => {
        console.log(newEmail, newCellphone, newAddress);
        props.updateAdmin(id as string, {
            email: newEmail,
            cellphone: newCellphone,
            address: newAddress,
            id: props.admin.id,
            employeeID: props.admin.employeeID,
            photo: props.admin.photo,
            name: props.admin.name,
            password: props.admin.password,
            username: props.admin.username
        })
    });

    React.useEffect(() => {
        console.log("run effect details");
        props.loadAdmin(id as string);
    }, []);
    return (
        <Container>
            <h1 className="text-center">Admin Details</h1>
            <br/>

            <Image className="float-left mr-5" width={"20%"} fluid src={require('../images/user.jpg')} roundedCircle/>

            <h5>Name: {props.admin.name}</h5><br/>
            <h5>Email: {props.admin.email}</h5><br/>
            <h5>Cellphone: {props.admin.cellphone}</h5><br/>

            <h1 className="text-center">Edit Admin</h1>
            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Cellphone</label>
                    <input type="number" min="0" className="form-control" id="newCellphone" name="newCellphone"
                           ref={register({required: true})}/>
                    {errors.adminName && 'Admin cellphone is required'}
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input className="form-control" id="newEmail" name="newEmail" ref={register({required: true})}/>
                    {errors.adminName && 'Admin email is required'}
                </div>
                <div className="form-group">
                    <label>Address</label>
                    <input className="form-control" id="newAddress" name="newAddress" ref={register({required: true})}/>
                    {errors.adminName && 'Admin address is required'}
                </div>
                <input className="btn btn-primary float-right" type="submit" value="Edit Admin Contact Info"/>
            </form>

        </Container>

    );
};


const mapStateToProps = (state: GlobalState) => {
    return {
        admin: state.admins.admin,
        isFetching: state.pets.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {
        loadAdmin: (id: string) => {
            dispatch(fetchAdmin(id))
        },
        updateAdmin: (id: string, newAdmin: Admin) => {
            dispatch(updateAdminRequest(id, newAdmin))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(AdminDetails);