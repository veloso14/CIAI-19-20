import React from 'react';
import {GlobalState} from "../App";
import {connect} from "react-redux";
import ListGroup from "react-bootstrap/ListGroup";
import {Link} from "react-router-dom";
import useForm from "react-hook-form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import {deleteAdminRequest, fetchAdmins, postAdmin} from "../actions/AdminActions";



export interface Admin {
    employeeID: number;
    photo: string;
    address: string;
    password: string;
    username: string;
    id: number,
    name: string,
    cellphone: number,
    email: string

}


export interface AdminState {
    admins: Admin[],
    admin: Admin,
    isFetching: boolean
}

type FormData = {
    adminName: string;
    adminCellphone: number;
    adminEmail: string;
    adminPhoto: string;
    adminAddress: string;
    adminPassword: string;
    adminUsername: string;


}


const AdminList = (props: { admins: Admin[], isFetching: boolean, loadAdmins: () => void, postAdmin: (admin: Admin) => void, deleteAdmin: (id: number) => void  }) => {
    const [update, setUpdate] = React.useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({adminName, adminCellphone, adminEmail, adminAddress, adminPhoto,adminUsername,adminPassword}) => {
        console.log(adminName, adminCellphone,adminEmail);
        props.postAdmin({
            address: adminAddress,
            employeeID: 0,
            id: 0,
            password: adminPassword,
            photo: adminPhoto,
            username: adminUsername,
            name: adminName, cellphone: adminCellphone, email: adminEmail });
        setUpdate(true);
        setValue("adminName", "");
        setValue("adminSpecies", "");
    });

    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadAdmins();
        return () => {
            setUpdate(false)
        }
    }, [update]);

    let list = props.admins.map((admin: Admin) => {
        return (
            <ListGroup.Item key={admin.id}>
                <Link to={`/admin/${admin.id}`}>{admin.name}</Link>
                <Button className="float-right" variant="primary" size="sm" onClick={() => {
                    props.deleteAdmin(admin.id);
                    setUpdate(true)
                }}>Delete</Button>
            </ListGroup.Item>
        )
    });

    return (
        <Container>
            <br/>
            <h1 className="text-center">Admins</h1>
            <br/>

            {props.isFetching ? <p>Loading...</p> : <ListGroup>{list}</ListGroup>}

            <br/>
            <h1 className="text-center">Hire new admin</h1>

            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Name</label>
                    <input className="form-control" id="adminName" name="adminName" ref={register({required: true})}/>
                    {errors.adminName && 'Admin name is required'}
                </div>
                <div className="form-group">
                    <label>Username</label>
                    <input className="form-control" id="adminUsername" name="adminUsername" ref={register({required: true})}/>
                    {errors.adminName && 'Admin username is required'}
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input className="form-control" id="adminPassword" name="adminPassword" ref={register({required: true})}/>
                    {errors.adminName && 'Admin password is required'}
                </div>
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
                <div className="form-group">
                    <label>Photo</label>
                    <input className="form-control" id="adminPhoto" name="adminPhoto" ref={register({required: true})}/>
                    {errors.adminName && 'Admin photo is required'}
                </div>

                <input className="btn btn-primary float-right" type="submit" value="Add Admin"/>
            </form>
        </Container>
    );
};

const mapStateToProps = (state: GlobalState) => ({
    admins: state.admins.admins,
    isFetching: state.admins.isFetching
});

const mapDispatchToProps = (dispatch: any) => {
    return {
        loadAdmins: () => {
            dispatch(fetchAdmins())
        },
        postAdmin: (admin: Admin) => {
            dispatch(postAdmin(admin))
        },
        deleteAdmin: (id: number) => {
            dispatch(deleteAdminRequest(id))
        }
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(AdminList);