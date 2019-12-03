import React from 'react';
import {GlobalState} from "../App";
import {connect} from "react-redux";
import {useParams} from "react-router-dom"
import useForm from "react-hook-form";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image"
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Admin} from "./AdminList";
import {fetchAdmin, updateAdminRequest} from "../actions/AdminActions";

type FormData = {
    newEmail: string;
    newCellphone: number;
    newAdress: string;
}

/*
* #TODO: Update pet request não funciona (erro 500)
* */

const AdminDetails = (props: { admin: Admin, loadAdmin: (id: string) => void, updateAdmin: (id: string, admin: Admin) => void }) => {
    let {id} = useParams();

    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({newEmail, newCellphone,newAdress}) => {
        console.log(newEmail, newCellphone,newAdress);
        props.updateAdmin(id as string, {email: newEmail, cellphone:newCellphone, address:newAdress, id: props.admin.id, employeeID: props.admin.employeeID, photo:props.admin.photo, name:props.admin.name, password: props.admin.password, username:props.admin.username})
    });

    React.useEffect(() => {
        console.log("run effect details");
        props.loadAdmin(id as string);
    }, []);
    return (
        <Container>
            <h1 className="text-center">Details</h1>
            <Row>
                <Image width={200} height={200} fluid src={require('../images/dog.jpg')} roundedCircle/>
                <Col>
                    <h5>Name: {props.admin.name}</h5><br/>
                    <h5>Email: {props.admin.email}</h5><br/>
                    <h5>Cellphone: {props.admin.cellphone}</h5><br/>
                </Col>
            </Row>
            <h1 className="text-center">Edit Admin</h1>
            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Cellphone</label>
                    <input type="number" min="0" className="form-control" id="adminCellphone" name="adminCellphone" ref={register({required: true})}/>
                    {errors.adminName && 'Admin cellphone is required'}
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input className="form-control" id="adminEmail" name="adminEmail" ref={register({required: true})}/>
                    {errors.adminName && 'Admin email is required'}
                </div>
                <div className="form-group">
                    <label>Address</label>
                    <input className="form-control" id="adminAddress" name="adminAddress" ref={register({required: true})}/>
                    {errors.adminName && 'Admin address is required'}
                </div>
                <input className="btn btn-primary float-right" type="submit" value="Edit Admin"/>
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